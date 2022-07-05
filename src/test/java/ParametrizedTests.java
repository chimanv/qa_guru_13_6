import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.util.Arrays.asList;

public class ParametrizedTests {
    @ValueSource(strings = {"Apple", "Samsung"})
    @ParameterizedTest(name = "При поиске по производителю {0} в результатах отображается товар бренда {0}")
    public void searchByCompany(String companyName) {
        Selenide.open("https://www.amazon.com/");
        $("#twotabsearchtextbox").setValue(companyName);
        $("#nav-search-submit-button").click();
        $$("div[data-component-type = s-search-result]").find(text(companyName)).shouldBe(visible);
    }

    @CsvSource(value = {
            "Apple Watch, Smart Watch",
            "Samsung Watch, Smartwatch"
    })
    @ParameterizedTest(name = "При поиске по производителю {0} в результатах отображается товар {1}")
    void searchDeviceByCompany(String companyName, String expectedResult) {
        Selenide.open("https://www.amazon.com/");
        $("#twotabsearchtextbox").setValue(companyName);
        $("#nav-search-submit-button").click();
        $$("div[data-component-type = s-search-result]").find(text(expectedResult)).shouldBe(visible);
    }

    static Stream<Arguments> checkResultsContainerDataProvider() {
        return Stream.of(
                Arguments.of("Apple", asList("apple")),
                Arguments.of("Samsung", asList("samsung"))
        );
    }

    @MethodSource(value = "checkResultsContainerDataProvider")
    @ParameterizedTest(name = "При поиске по производителю {0} в результатах поиска отображается текст {1}")
    void checkResultsContainer(String companyName, List<String> expectedResult) {
        Selenide.open("https://www.amazon.com/");
        $("#twotabsearchtextbox").setValue(companyName);
        $(".autocomplete-results-container").shouldBe(visible);
        $$(".autocomplete-results-container").shouldHave(CollectionCondition.texts(expectedResult));
    }

    @EnumSource(Brands.class)
    @ParameterizedTest(name = "При поиске по производителю {0} в результатах отображается товар бренда {0}")
    public void searchByCompanyWithEnum(Brands brands) {
        Selenide.open("https://www.amazon.com/");
        $("#twotabsearchtextbox").setValue(brands.name);
        $("#nav-search-submit-button").click();
        $$("div[data-component-type = s-search-result]").find(text(brands.name)).shouldBe(visible);
    }
}
