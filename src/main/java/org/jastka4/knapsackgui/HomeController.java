package org.jastka4.knapsackgui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Menu;
import javafx.scene.control.Label;
import org.jastka4.knapsack.*;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
	@FXML private Menu language;
	@FXML private Label currentDate;
	@FXML private TextField capacity;
	@FXML private TextField name;
	@FXML private TextField value;
	@FXML private TextField weight;
	@FXML private ListView<Item> items;
	@FXML private ComboBox<KnapsackAlgorithmConstants> algorithmComboBox;
	@FXML private ListView<Item> solutionItems;
	@FXML private TextField maxValue;

	private static KnapsackAlgorithmFactory knapsackAlgorithmFactory;

	public void addItem() {
		final Item item = new Item(name.getText(), new BigDecimal(value.getText()), Integer.parseInt(weight.getText()));
		items.getItems().add(item);
	}

	public void solve() {
		final int selectedCapacity = Integer.parseInt(capacity.getText());
		final KnapsackAlgorithmConstants selectedAlgorithm = algorithmComboBox.getSelectionModel().getSelectedItem();
		final List<Item> selectedItems = items.getItems();

		final ProblemInstance problemInstance = new ProblemInstance(selectedItems, selectedCapacity);
		final KnapsackAlgorithm knapsackAlgorithm = knapsackAlgorithmFactory.createKnapsackAlgorithm(selectedAlgorithm, problemInstance);
		final Solution solution = knapsackAlgorithm.solve();

		solutionItems.getItems().setAll(solution.getItems());
		maxValue.setText(solution.getValue().toString());
	}

	public void changeLanguage(ActionEvent actionEvent) {
	}

	public void close() {
		Platform.exit();
	}

	public void getAbout(ActionEvent actionEvent) {
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		knapsackAlgorithmFactory = new KnapsackAlgorithmFactory();

		initializeListView(items);
		initializeListView(solutionItems);
		initializeAlgorithmComboBox();

		LocalDate date = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		currentDate.setText(date.format(dateTimeFormatter));
	}

	private void initializeListView(final ListView<Item> listView) {
		final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
		listView.setCellFactory((ListView<Item> param) -> new ListCell<>() {
			@Override
			protected void updateItem(Item item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
				} else {
					String text = new StringBuilder()
							.append(item.getName())
							.append(", ").append(numberFormat.format(item.getValue()))
							.append(", ").append(item.getWeight())
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
