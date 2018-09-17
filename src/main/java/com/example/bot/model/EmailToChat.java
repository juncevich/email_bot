package com.example.bot.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailToChat extends MessageCandidate {
    private String userId;
}
