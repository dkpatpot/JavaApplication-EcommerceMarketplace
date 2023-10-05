package ku.cs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import com.github.saacsos.FXRouter;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "Kanison Shop");
        configRoute();
        FXRouter.goTo("login");
    }

    private static void configRoute() {
        String packageStr = "ku/cs/";
        FXRouter.when("login", packageStr+ "login.fxml");
        FXRouter.when("register", packageStr+ "register.fxml");
        FXRouter.when("marketplace", packageStr+ "marketplace.fxml");
        FXRouter.when("admin_manager", packageStr+ "admin_manager.fxml");
        FXRouter.when("change_profile",packageStr+ "change_profile.fxml");
        FXRouter.when("create_store",packageStr+ "create_store.fxml");
        FXRouter.when("add_product",packageStr+ "add_product.fxml");
        FXRouter.when("create_profile",packageStr+ "create_profile.fxml");
        FXRouter.when("edit_store",packageStr+ "edit_store.fxml");
        FXRouter.when("order_list",packageStr+ "order_list.fxml");
        FXRouter.when("store",packageStr+ "store.fxml");
        FXRouter.when("product_detail",packageStr+ "product_detail.fxml");
        FXRouter.when("edit_product",packageStr+ "edit_product.fxml");
        FXRouter.when("confirm_purchase",packageStr+ "confirm_purchase.fxml");
        FXRouter.when("product_report",packageStr+ "product_report.fxml");
        FXRouter.when("review_report",packageStr+ "review_report.fxml");
        FXRouter.when("seller_marketplace",packageStr+ "seller_marketplace.fxml");
        FXRouter.when("change_password",packageStr+ "change_password.fxml");
        FXRouter.when("reported_user",packageStr+ "reported_user.fxml");
        FXRouter.when("add_tracking_number",packageStr+ "add_tracking_number.fxml");
        FXRouter.when("create_promotion_code",packageStr+ "create_promotion_code.fxml");
        FXRouter.when("creator",packageStr+ "creator.fxml");
        FXRouter.when("suggestion",packageStr+"suggestion.fxml");
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}