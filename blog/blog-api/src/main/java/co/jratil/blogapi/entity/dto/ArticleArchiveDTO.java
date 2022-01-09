package co.jratil.blogapi.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wenjunjun9
 * @created 2021/12/19 15:54
 */
@Data
public class ArticleArchiveDTO implements Serializable {

    private String archive;

    private Integer count;
}
