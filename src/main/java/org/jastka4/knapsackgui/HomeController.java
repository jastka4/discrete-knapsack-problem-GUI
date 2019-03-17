package org.jastka4.knapsackgui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.jastka4.knapsack.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class HomeController implements Initializable {
	@FXML private VBox content;
	@FXML private Menu languagesMenu;
	@FXML private Label currentDate;
	@FXML private Label language;
	@FXML private TextField capacity;
	@FXML private TextField name;
	@FXML private TextField value;
	@FXML private TextField weight;
	@FXML private ListView<Item> itemsListView;
	@FXML private Label itemsNumber;
	@FXML private ComboBox<KnapsackAlgorithmConstants> algorithmComboBox;
	@FXML private ListView<Item> solutionItems;
	@FXML private TextField maxValue;

	private static KnapsackAlgorithmFactory knapsackAlgorithmFactory = new KnapsackAlgorithmFactory();
	private static List<Item> items = new ArrayList<>();

	public void addItem() {
		final Item item = new Item(name.getText(), new BigDecimal(value.getText()), Integer.parseInt(weight.getText()));
		itemsListView.getItems().add(item);
		updateItemsNumber(itemsListView.getItems().size());
	}

	public void solve() {
		final int selectedCapacity = Integer.parseInt(capacity.getText());
		final KnapsackAlgorithmConstants selectedAlgorithm = algorithmComboBox.getSelectionModel().getSelectedItem();
		final List<Item> selectedItems = itemsListView.getItems();

		final ProblemInstance problemInstance = new ProblemInstance(selectedItems, selectedCapacity);
		final KnapsackAlgorithm knapsackAlgorithm = knapsackAlgorithmFactory.createKnapsackAlgorithm(selectedAlgorithm, problemInstance);
		final Solution solution = knapsackAlgorithm.solve();

		solutionItems.getItems().setAll(solution.getItems());
		maxValue.setText(solution.getValue().toString());
	}

	public void changeLanguage(Locale locale) {
		I18N.setLocale(locale);
		try {
			items = itemsListView.getItems();
			content.getScene().setRoot(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/home.fxml"), I18N.getResource()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		Platform.exit();
	}

	public void getAbout() {
		Parent about;
		try {
			about = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/about.fxml"), I18N.getResource());
			Stage stage = new Stage();
			stage.titleProperty().bind(I18N.createStringBinding("about.title"));
			stage.setScene(new Scene(about));
			stage.show();
		} catch (IOException e) {
				e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		if (Objects.nonNull(items) && !items.isEmpty()) {
			itemsListView.getItems().setAll(items);
		}
		initializeItemsListView(itemsListView);
		updateItemsNumber(itemsListView.getItems().size());
		initializeItemsListView(solutionItems);
		initializeAlgorithmComboBox();
		initializeLocalesMenu();

		LocalDate date = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		currentDate.setText(date.format(dateTimeFormatter));
		language.setText(I18N.getLocale().getLanguage() + "/" +I18N.getLocale().getCountry());
	}

	private void updateItemsNumber(int size) {
		if (0 == size || 5 <= size) {
			itemsNumber.setText(I18N.get("item.number.zero_multiple", size));
		} else if (0 < size && size < 5) {
			itemsNumber.setText(I18N.get("item.number.less_than_5", size));
		} else {
			itemsNumber.setText(I18N.get("item.number.one", size));
		}
	}

	private void initializeItemsListView(final ListView<Item> listView) {
		final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(I18N.getLocale());
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

	private void initializeLocalesMenu() {
		I18N.getSupportedLocales().forEach(locale -> {
			if (!locale.equals(I18N.getLocale())) {
				MenuItem menuItem = new MenuItem(locale.getDisplayName());
				menuItem.setOnAction(event -> changeLanguage(locale));
				languagesMenu.getItems().add(menuItem);
			}
		});
	}

	private void initializeAlgorithmComboBox() {
		algorithmComboBox.getItems().addAll(KnapsackAlgorithmConstants.BRUTE_FORCE,
				KnapsackAlgorithmConstants.DYNAMIC_PROGRAMMING,
				KnapsackAlgorithmConstants.GREEDY_ALGORITHM,
				KnapsackAlgorithmConstants.RANDOM_SEARCH);

		algorithmComboBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(KnapsackAlgorithmConstants algorithmConstants) {
				return I18N.get("algorithm." + algorithmConstants.name());
			}

			@Override
			public KnapsackAlgorithmConstants fromString(String s) {
				throw new UnsupportedOperationException();
			}
		});
	}
}
