package org.request;


import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookRequestDTO(@NotNull String title,
                             @NotNull List<Long> authorIds,
                             String image) {
}
