package com.logistics.agent.admin.api;

import java.time.Instant;

public record ApiError(
    Instant timestamp,
    int status,
    String code,
    String message
) {
}
