package org.jastka4.knapsackgui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	public void openGithub(ActionEvent actionEvent) {
		try {
			Desktop.getDesktop().browse(new URL("https://github.com/jastka4").toURI());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
