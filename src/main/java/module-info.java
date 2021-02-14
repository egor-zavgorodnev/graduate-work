module translator {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.logging;
    requires log4j;
    requires asm.all;

    opens ru.tver.tstu;
    opens ru.tver.tstu.controllers;
    opens ru.tver.tstu.fxml;
}
