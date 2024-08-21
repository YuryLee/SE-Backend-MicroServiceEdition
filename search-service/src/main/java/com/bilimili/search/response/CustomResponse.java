package com.bilimili.search.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @author Yury
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// 自定义响应对象
public class CustomResponse {
    private int code = 200;
    private String message = "OK";
    private Object data;
}