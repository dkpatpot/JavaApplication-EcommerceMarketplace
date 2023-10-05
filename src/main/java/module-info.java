module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;

    opens ku.cs to javafx.fxml;
    exports ku.cs;

    exports ku.cs.kanison.controllers.user;
    opens ku.cs.kanison.controllers.user to javafx.fxml;

    exports ku.cs.kanison.controllers.admin;
    opens ku.cs.kanison.controllers.admin to javafx.fxml;

    exports ku.cs.kanison.models.account;
    opens ku.cs.kanison.models.account to javafx.fxml;

    exports ku.cs.kanison.controllers.marketplace;
    opens ku.cs.kanison.controllers.marketplace to javafx.fxml;

    exports ku.cs.kanison.controllers.login;
    opens ku.cs.kanison.controllers.login to javafx.fxml;

    exports ku.cs.kanison.models.product;
    opens ku.cs.kanison.models.product to javafx.fxml;
    exports ku.cs.kanison.controllers.report;
    opens ku.cs.kanison.controllers.report to javafx.fxml;

}
