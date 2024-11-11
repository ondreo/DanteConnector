package pl.ondreo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BitDepth {
    B16(16),
    B24(24),
    B32(32);

    private final int value;
}
