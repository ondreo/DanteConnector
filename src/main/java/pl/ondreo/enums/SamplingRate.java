package pl.ondreo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SamplingRate {
    K44_1(44100),
    K48(48000),
    K88_2(88200),
    K96(96000),
    K176_4(176400),
    K192(192000);

    private final int value;
}
