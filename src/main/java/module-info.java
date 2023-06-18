module com.utfpr.distributed {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires java.logging;
    requires java.sql;

    opens com.utfpr.distributed to javafx.fxml;
    exports com.utfpr.distributed;
    exports com.utfpr.distributed.database;
    opens com.utfpr.distributed.database to javafx.fxml;
    exports com.utfpr.distributed.util;
    opens com.utfpr.distributed.util to javafx.fxml;
    exports com.utfpr.distributed.model;
    opens com.utfpr.distributed.model to javafx.fxml;
    exports com.utfpr.distributed.service;
    opens com.utfpr.distributed.service to javafx.fxml;
    exports com.utfpr.distributed.exception;
    opens com.utfpr.distributed.exception to javafx.fxml;
    exports com.utfpr.distributed.controller;
    opens com.utfpr.distributed.controller to javafx.fxml;
    exports com.utfpr.distributed.util.socket;
    opens com.utfpr.distributed.util.socket to javafx.fxml;
}