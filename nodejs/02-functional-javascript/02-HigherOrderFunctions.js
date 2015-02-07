function repeat(op, num) {
  if (num > 0) {
     op();
     repeat(op, num-1);
  }
}

module.exports = repeat;
