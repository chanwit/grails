class DynamicScaffoldingTest extends grails.util.WebTest {

    void suite() {
        testDynamicCRUD()
    }

    def testDynamicCRUD() {
        webtest("Dynamic CRUD") {
            invoke url: "car/list?lang=en"

            clickLink "New Car"
            setSelectField name: "dateOfManufacture_day", text: "1"
            setSelectField name: "dateOfManufacture_month", text: "January"
            setSelectField name: "dateOfManufacture_year", text: "2001"
            setSelectField name: "dateOfManufacture_year", text: "2020"
            setSelectField name: "dateOfManufacture_hour", text: "12"
            setSelectField name: "dateOfManufacture_minute", text: "00"
            setInputField name: "engineCapacity", value: "12345"
            setInputField name: "make", value: "Ferrari"
            setInputField name: "listPrice", value: "135456.88"
            setInputField name: "mpg", value: "25.5"
            clickButton "Create"

            verifyText "2020-01-01 12:00:00.0"
            verifyText "12,345"
            verifyText "Ferrari"
            verifyText "135,456.88"
            verifyText "25.5"
            verifyTitle "Show Car"
            clickButton "Edit"

            setInputField name: "engineCapacity", value: "54321"
            clickButton "Update"

            verifyTitle "Show Car"
            verifyText "54,321"

            invoke url: "car/list"
            verifyText "Ferrari"

            // Check what the list price and MPG look like in a different locale.
            invoke url: "car/list?lang=de"
            clickLink xpath: "//tr[td[2]='Ferrari']/td[1]/a"
            verifyText "135.456,88"
            verifyText "25,5"

            // The edit page should also be showing the correctly formatted
            // number.
            clickButton "Edit"
            verifyText "135.456,88"
            verifyText "25,5"

            // Make sure we can change the value using the localised form.
            setInputField name: "listPrice", value: "16.000,55"
            setInputField name: "mpg", value: "30,9"
            clickButton "Update"

            verifyTitle "Show Car"
            verifyText "16.000,55"
            verifyText "30,9"

            clickButton "Delete"

            verifyTitle "Car List"
            not {
                verifyText("Ferrari")
            }

            // Now make sure that i18n is working by going to the list
            // page and limiting the maximum number of cars it shows so
            // that the "next" link is displayed.
            //
            invoke url: "car/list?max=2"
            verifyText "Nächste"

            // Set language back to English.
            invoke url: "car/list?lang=en"
        }
    }

}
