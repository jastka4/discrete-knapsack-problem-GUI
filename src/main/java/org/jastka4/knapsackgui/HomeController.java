package org.jastka4.knapsackgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.jastka4.knapsack.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
	@FXML private TextField capacity;
	@FXML private TextField name;
	@FXML private TextField value;
	@FXML private TextField weight;
	@FXML private ListView<Item> items;
	@FXML private ComboBox<KnapsackAlgorithmConstants> algorithmComboBox;

	public void addItem(ActionEvent actionEvent) {
		final Item item = new Item(name.getText(), new BigDecimal(value.getText()), Integer.parseInt(weight.getText())); // TODO - add NumberFormat for internationalization
		items.getItems().add(item);
	}

	public void solve(ActionEvent actionEvent) {
		final int selectedCapacity = Integer.parseInt(capacity.getText());
		final KnapsackAlgorithmConstants selectedAlgorithm = algorithmComboBox.getSelectionModel().getSelectedItem();
		final List<Item> selectedItems = items.getItems();

		KnapsackAlgorithmFactory knapsackAlgorithmFactory = new KnapsackAlgorithmFactory();
		ProblemInstance problemInstance = new ProblemInstance(selectedItems, selectedCapacity);
		KnapsackAlgorithm knapsackAlgorithm = knapsackAlgorithmFactory.createKnapsackAlgorithm(selectedAlgorithm, problemInstance);
		Solution solution = knapsackAlgorithm.solve();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeListView();
		initializeAlgorithmComboBox();
	}

	private void initializeListView() {
		items.setCellFactory((ListView<Item> param) -> new ListCell<>() {
			@Override
			protected void updateItem(Item item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
				} else {
					String text = new StringBuilder()
							.append(item.getName())
							.append(", ").append(item.getValue().setScale(2, RoundingMode.HALF_UP)).append("zł")    // TODO - add internationalization
							.append(", ").append(item.getWeight()).append("g")  // TODO - add internationalization ??
							.toString();
					setText(text);
				}
			}
		});
	}

	private void initializeAlgorithmComboBox() {
		algorithmComboBox.getItems().addAll(KnapsackAlgorithmConstants.BRUTE_FORCE,
				KnapsackAlgorithmConstants.DYNAMIC_PROGRAMMING,
				KnapsackAlgorithmConstants.GREEDY_ALGORITHM,
				KnapsackAlgorithmConstants.RANDOM_SEARCH);
	}
}
