package com.plq.grammarly.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class MailRequest implements Serializable {

    private String sendTo;
    private String subject;
    private String content;
    private String bcc;
    private List<String> filePaths;

}
