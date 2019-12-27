package co.jratil.blogauth.service;

import co.jratil.blogapi.constant.VerifyCodeConstant;
import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogapi.entity.dataobject.*;
import co.jratil.blogapi.entity.dto.AuthorDTO;
import co.jratil.blogapi.entity.dto.AuthorForm;
import co.jratil.blogapi.enums.UserRoleEnum;
import co.jratil.blogapi.exception.GlobalException;
import co.jratil.blogapi.service.AbstractService;
import co.jratil.blogapi.service.RedisService;
import co.jratil.blogauth.mapper.AuthorLoginMapper;
import co.jratil.blogauth.mapper.AuthorMapper;
import co.jratil.blogapi.enums.ResponseEnum;
import co.jratil.blogapi.service.AuthorService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service(interfaceClass = AuthorService.class)
@Component
@Slf4j
public class AuthorServiceImpl extends AbstractService implements AuthorService {

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private AuthorLoginMapper loginMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Reference(interfaceClass = RedisService.class)
    private RedisService redisService;

    /**
     * 暂时废弃，使用spring security的登录过滤
     *
     * @param account
     * @param password
     * @param ipAddr
     * @return
     */
    @Transactional
    @Override
    public AuthorDTO login(String account, String password, String ipAddr) {

        // 判断是否存在以及密码正确
        AuthorDTO authorDTO = getByAccount(account);
        if (authorDTO != null && !passwordEncoder.encode(password).equals(authorDTO.getAuthorPassword())) {
            log.error("【用户处理】登录失败，账号或密码错误，account={},password={}", account, password);
            throw new GlobalException(ResponseEnum.LOGIN_ERROR);
        }

        // 更新用户登录的 ip 地址和时间
        AuthorLogin authorLogin = new AuthorLogin();
        authorLogin.setAuthorId(authorDTO.getAuthorId());
        authorLogin.setLoginIp(ipAddr);
        loginMapper.insertOrUpdate(authorLogin);

        return authorDTO;
    }

    /**
     * 用户注册
     *
     * @param authorForm
     */
    @Transactional
    @Override
    public void register(AuthorForm authorForm) {

        // 判断验证码是否输入正确
        String account = authorForm.getAuthorAccount();
        verifyCode(account, authorForm.getVerifyCode());

        // 设置用户信息
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setAuthorName(authorForm.getAuthorName());
        authorDTO.setAuthorAccount(account);
        authorDTO.setAuthorPassword(passwordEncoder.encode(authorForm.getAuthorPassword()));

        // 插入用户
        insertAuthor(authorDTO);
    }


    /**
     * 根据authorId查询用户
     *
     * @param authorId 用户id
     * @return 用户信息
     */
    @Cacheable(value = "getById", key = "#authorId")
    @Override
    public AuthorDTO getById(Integer authorId) {
        Author author = authorMapper.selectById(authorId);
        if (author == null) {
            log.error("【用户处理】查询用户出错，用户不存在，authorId={}", authorId);
            throw new GlobalException(ResponseEnum.AUTHOR_NOT_EXIST);
        }

        return doToDto(author);
    }

    /**
     * 根据账号查询用户
     *
     * @param account
     * @return
     */
    @Cacheable(value = "getByAccount", key = "#account")
    @Override
    public AuthorDTO getByAccount(String account) {
        Author author = authorMapper.selectOne(new QueryWrapper<Author>().eq("author_account", account));
        if (author == null) {
            log.error("【用户处理】查询用户出错，用户不存在，account={}", account);
            throw new GlobalException(ResponseEnum.AUTHOR_NOT_EXIST);
        }
        return doToDto(author);
    }

    /**
     * 根据用户名查询用户
     *
     * @param name
     * @return
     */
    @Cacheable(value = "getByName", key = "#name")
    @Override
    public AuthorDTO getByName(String name) {
        Author author = authorMapper.selectOne(new QueryWrapper<Author>().eq("author_name", name));

        if (author == null) {
            log.error("【用户处理】查询用户出错，用户不存在，name={}", name);
            throw new GlobalException(ResponseEnum.AUTHOR_NOT_EXIST);
        }

        return doToDto(author);
    }

    /**
     * 根据名字模糊查询用户
     *
     * @param blurName 模糊查询的名字
     * @return 查询到的用户列表
     */
    @Override
    public PageInfo<AuthorDTO> listByBlueName(PageParam pageParam, String blurName) {
        // 构造模糊查询条件
        QueryWrapper<Author> wrapper = new QueryWrapper<>();
        wrapper.like("author_name", blurName);

        // 进行查询
        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<Author> authorList = authorMapper.selectList(wrapper);

        return dosToDtos(authorList);
    }

    /**
     * 查询全部用户
     *
     * @return
     */
    public PageInfo<AuthorDTO> list(PageParam pageParam) {

        PageHelper.startPage(pageParam.getPage(), pageParam.getCount());
        List<Author> authorList = authorMapper.selectList(Wrappers.<Author>lambdaQuery().orderByDesc(Author::getCreateTime));

        return dosToDtos(authorList);
    }


    /**
     * 添加用户
     *
     * @param authorDTO
     */
    @Caching(evict = {
            @CacheEvict(value = "getById", allEntries = true),
            @CacheEvict(value = "getByAccount", allEntries = true),
            @CacheEvict(value = "getByName", allEntries = true)
    })
    @Transactional
    @Override
    public void insertAuthor(AuthorDTO authorDTO) {

        Author author = dtoToDo(authorDTO);
        // 插入成功会返回authorId
        int authorId = authorMapper.insertAuthor(author);

        // 数据库插入使用了ignore, 所以出现相同的会忽略
        // 插入用户数据失败则中断操作，抛出异常
        if (authorId < 1) {
            log.error("【用户处理】注册失败，账号或昵称可能已存在，name={},account={}", authorDTO.getAuthorName(), authorDTO.getAuthorAccount());
            throw new GlobalException(ResponseEnum.AUTHOR_EXIST);
        }

        // 生成权限信息
        AuthorRole authorRole = new AuthorRole();
        authorRole.setAuthorId(author.getAuthorId());
        authorRole.setRole(UserRoleEnum.ROLE_USER.getUserRole());
        authorRole.setLastUpdate(new Date());
        authorMapper.insertRole(authorRole);

        ArticleCategory category = new ArticleCategory();
        category.setAuthorId(author.getAuthorId());
        category.setCategoryVisible(true);
        category.setCategoryName("默认");
        category.setCreateTime(new Date());
        authorMapper.insertCategory(category);
    }

    @Caching(evict = {
            @CacheEvict(value = "getById", key = "#authorDTO.getAuthorId()"),
            @CacheEvict(value = "getByAccount", key = "#authorDTO.getAuthorAccount()"),
            @CacheEvict(value = "getByName", allEntries = true)
    })
    @Transactional
    @Override
    public void updateAuthor(AuthorDTO authorDTO) {

        // 判断用户是否存在
        Author result = authorMapper.selectOne(Wrappers.<Author>lambdaQuery()
                .eq(Author::getAuthorId, authorDTO.getAuthorId())
                .eq(Author::getAuthorAccount, authorDTO.getAuthorAccount()));
        if (result == null) {
            log.error("【用户处理】更新用户出错，用户不存在");
            throw new GlobalException(ResponseEnum.AUTHOR_NOT_EXIST);
        }

        int i = authorMapper.updateById(dtoToDo(authorDTO));
        if (i != 1) {
            log.error("【用户处理】更新用户信息失败，author={}", authorDTO);
            throw new GlobalException(ResponseEnum.UPDATE_ERROR);
        }
    }

    @CacheEvict(value = "getByAccount", key = "#authorForm.getAuthorAccount()")
    @Transactional
    @Override
    public void updatePassword(AuthorForm authorForm) {

        // 判断验证码是否正确
        String account = authorForm.getAuthorAccount();
        verifyCode(account, authorForm.getVerifyCode());

        getByAccount(authorForm.getAuthorAccount());

        Author author = new Author();
        author.setAuthorAccount(authorForm.getAuthorAccount());
        author.setAuthorPassword(passwordEncoder.encode(authorForm.getAuthorPassword()));

        // 根据用户账号更新密码
        authorMapper.update(author, Wrappers.<Author>lambdaUpdate().eq(Author::getAuthorAccount, account));
    }

    @Caching(evict = {
            @CacheEvict(value = "getById", allEntries = true),
            @CacheEvict(value = "getByAccount", allEntries = true),
            @CacheEvict(value = "getByName", allEntries = true)
    })
    @Transactional
    @Override
    public void deleteAuthor(Integer authorId) {

        // 判断用户是否存在
        getById(authorId);
        int result = authorMapper.deleteById(authorId);
        if (result < 1) {
            log.error("【用户处理】删除用户出错，用户不存在，authorId={}", authorId);
            throw new GlobalException(ResponseEnum.AUTHOR_NOT_EXIST);
        }
    }

    /**
     * 判断验证码是否正确
     *
     * @param account    账户，用来从redis中获取到该账户的验证码
     * @param verifyCode 用户输入的验证码
     */
    private void verifyCode(String account, String verifyCode) {

        //TODO----验证码开关代码
        Object redisVerifyCode = redisService.get(VerifyCodeConstant.REDIS_VERIFY_PREFIX + account);
        if (!verifyCode.equals(redisVerifyCode)) {
            log.error("【用户注册】验证码不正确，verifyCode={}", verifyCode);
            throw new GlobalException(ResponseEnum.VERIFY_NOT_EQUAL);
        }
    }

    /**
     * 将Author 转为 AuthorDTO
     *
     * @param author
     * @return
     */
    private AuthorDTO doToDto(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        BeanUtils.copyProperties(author, authorDTO);
        return authorDTO;
    }

    /**
     * 将AuthorDTO 转为Author
     *
     * @param authorDTO
     * @return
     */
    private Author dtoToDo(AuthorDTO authorDTO) {
        Author author = new Author();
        BeanUtils.copyProperties(authorDTO, author);
        return author;
    }

    private PageInfo<AuthorDTO> dosToDtos(List<Author> authors) {
        // 先将 list 放到 pageInfo 中，以获取分页的参数以及 total
        PageInfo<Author> doPage = new PageInfo<>(authors);

        // 将 list<do> 转换为 list<dto>
        List<AuthorDTO> list = doPage.getList().stream()
                .map(item -> {
                    AuthorDTO dto = new AuthorDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        // 设置 dtoPage 的参数
        PageInfo<AuthorDTO> dtoPage = new PageInfo<>();
        BeanUtils.copyProperties(doPage, dtoPage);
        dtoPage.setList(list);
        dtoPage.setTotal(doPage.getTotal());

        return dtoPage;
    }
}
