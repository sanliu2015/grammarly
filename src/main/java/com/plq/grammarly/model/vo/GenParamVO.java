package com.plq.grammarly.model.vo;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenParamVO {

    @NotNull(message = "数量不能为空")
    private Integer count;

    @NotNull(message = "有效天数不能为空")
    @Min(value = 1, message = "有效天数至少为1")
    private Integer validDays;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date exchangeDeadline;

}
