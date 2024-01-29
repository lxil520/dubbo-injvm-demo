package org.example.dto;

import java.io.Serial;
import java.io.Serializable;

public class DemoResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1293068791872081256L;

    private String name;
    private Integer age;
    private String address;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
