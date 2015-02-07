function logger(namespace) {
   return console.log.bind(console, namespace);
}

module.exports = logger

/* tests:
info = logger("INFO: ");
warn = logger("WARN: ");
info("test info msg");
warn("test warning msg", "bad stuff happens");
info("test", "foo", "bar");
warn("test warning msg", "with", "lots", "of", "args", 1, 2, [3]);
*/