firefox.exe --start-debugger-server --profile D:\selenium\firefoxTMP
chrome.exe --remote-debugging-port=9222 --user-data-dir="D:\selenium\chrome_data"
msedge.exe --remote-debugging-port=9333 --user-data-dir="D:\selenium\msedge_data"

https://github.com/mozilla/geckodriver/releases
https://www.selenium.dev/documentation/webdriver/drivers/options/