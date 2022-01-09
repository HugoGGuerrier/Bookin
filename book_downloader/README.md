# Bookin : Book Downloader

This is a standalone application for the Bookin website that allows you to download books from open source book source
For now it only supports the Gutenberg project but more sources will come in the future

## Dependencies

* JDK 11 : The java development kit 11
* maven : [The java building tool](https://maven.apache.org/install.html)

## How to compile the project

* Just run the command `mvn install clean package`
* The executable jar should be in the target folder

## How to use the book downloader

Run the jar a first time, it will generate a base `config.json` file that you can complete to perform book downloading\
All options are explained here :
* `provider` => The book provider you want to fetch from in [`"gutenberg"`]
* `lang` => The list of the language you want in [`"fr"` | `"en"`]
* `exportPath` => The path to the export directory where you want to download the books
* `minWordCount` => The minimum of word for a book to have to be downloaded
* `bookCount` => The number of book you want to download (-1 is for all books)