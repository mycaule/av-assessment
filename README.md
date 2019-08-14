## Adevinta Coding Exercise

### Pre-Requisites

- Java >= 8
- Maven >= 3

## Build

Buiding an archive
```bash
$ mvn clean package
```

Running unit tests
```bash
$ mvn test
```

Running the application
```bash
$ java -cp target/SimpleSearch-1.0.jar Searcher songs

4 files read in directory songs
search> remember
September.txt: 100%
search> boy
Upside down.txt: 100%
search> afraid
I will survive.txt: 100%
search> will
I will survive.txt: 100%
Sir Duke.txt: 100%
search> I will survive
I will survive.txt: 100%
Sir Duke.txt: 66%
Upside down.txt: 33%
search> adevinta
no matches found
search> :quit
```

## Comments

My algorithm uses these basic principles:
- split the files into atomic words then compare them to the words given in the query,
- case insensitive matching,
- a counter is incremented when a query word has been found.

To improve the scoring function :
- define a formula that takes into account the sequence of query words with respect to the order,
- use fuzzy matching for words, with Levenshtein distance.

See also recent work [`ripgrep`](https://github.com/BurntSushi/ripgrep) for state of the art of the most efficient algorithms.
