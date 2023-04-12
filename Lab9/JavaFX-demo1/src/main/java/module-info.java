module sustech.cs209.javafxdemo1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens sustech.cs209.javafxdemo1 to javafx.fxml;
    exports sustech.cs209.javafxdemo1;
}