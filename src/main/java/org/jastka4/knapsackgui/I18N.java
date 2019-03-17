package org.jastka4.knapsackgui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.text.MessageFormat;
import java.util.*;

/**
 * I18N utility class.
 */
public final class I18N {

	/**
	 * the current selected Locale.
	 */
	private static final ObjectProperty<Locale> locale;
	private static ResourceBundle resource;

	static {
		locale = new SimpleObjectProperty<>(getDefaultLocale());
		resource = ResourceBundle.getBundle("bundles.base", locale.get());

		locale.addListener((observable, oldValue, newValue) -> {
			Locale.setDefault(newValue);
			resource = ResourceBundle.getBundle("bundles.base", locale.get());
		});
	}

	/**
	 * Get the supported Locales.
	 *
	 * @return List of Locale objects.
	 */
	public static List<Locale> getSupportedLocales() {
		return new ArrayList<>(Arrays.asList(
				new Locale("pl", "PL"), new Locale("en", "GB"), new Locale("en", "US")));
	}

	/**
	 * Get the default locale. This is the systems default if contained in the supported locales, english otherwise.
	 *
	 * @return
	 */
	public static Locale getDefaultLocale() {
		Locale sysDefault = Locale.getDefault();
		return getSupportedLocales().contains(sysDefault) ? sysDefault : new Locale("en", "US");
	}

	public static Locale getLocale() {
		return locale.get();
	}

	public static void setLocale(Locale locale) {
		localeProperty().set(locale);
		Locale.setDefault(locale);
	}

	public static ObjectProperty<Locale> localeProperty() {
		return locale;
	}

	public static ResourceBundle getResource() {
		return resource;
	}

	public static void setResource(ResourceBundle resource) {
		I18N.resource = resource;
	}

	/**
	 * Gets the string with the given key from the resource bundle for the current locale and uses it as first argument
	 * to MessageFormat.format, passing in the optional args and returning the result.
	 *
	 * @param key  message key
	 * @param args optional arguments for the message
	 * @return localized formatted string
	 */
	public static String get(final String key, final Object... args) {
		return MessageFormat.format(resource.getString(key), args);
	}

	/**
	 * Creates a String binding to a localized String for the given message bundle key
	 *
	 * @param key key
	 * @return String binding
	 */
	public static StringBinding createStringBinding(final String key, Object... args) {
		return Bindings.createStringBinding(() -> get(key, args), locale);
	}
}
