package edu.flightregistration.core.domain;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

public class Terminal {

    private @Getter
    final String terminalNumber;

    private @Getter @Setter
    String flightNumber;

    public Terminal(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(flightNumber);
    }
}
