package co.jratil.blogapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-17 21:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParam implements Serializable {

    private static final long serialVersionUID = -3760386831990208962L;

    private int page;

    private int count;
}
