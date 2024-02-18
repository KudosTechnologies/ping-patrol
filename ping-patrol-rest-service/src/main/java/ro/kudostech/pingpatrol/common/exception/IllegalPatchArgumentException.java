package ro.kudostech.pingpatrol.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IllegalPatchArgumentException extends RuntimeException {
    private final Class<?> target;
}
