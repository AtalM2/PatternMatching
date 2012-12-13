; Copyright (C) 2012 by Grégoire Jadi
; See the file LICENSE for copying permission.

(in-package #:exact-string-matching)

(defun extract-alphabet (file)
  "Extracs the alphabet from the given FILE."
  (with-open-file (stream
                   file
                   :direction :input
                   :if-does-not-exist :error)
    (let ((alphabet (list)))
      (iter (for char = (read-char stream nil 'eof))
        (until (eq char 'eof))
        (pushnew char alphabet))
      (coerce alphabet 'vector))))

(defun make-patterns (alphabet n size)
  "Makes N patterns of SIZE from the given ALPHABET."
  (let ((*random-state* (make-random-state t)))
    (iter (with len = (length alphabet))
      (repeat n)
      (collect
          (coerce
           (iter (repeat size)
             (collect (aref alphabet (random len))))
           'string)))))

(defparameter *patterns-spec* '(8 (2 4 8 16 32 64 128 256))
  "*patterns-spec* ::= (N Sizes)

Where Sizes is a list of size of patterns to be generated and N the
number of patterns of each size to be generated.")

(defun gen-file-patterns (file patterns-spec)
  "Generates all patterns from file."
  (let ((alphabet (extract-alphabet file)))
    (iter (for size in (second patterns-spec))
      (appending (make-patterns alphabet (first patterns-spec) size)))))

(defparameter *files*
  '("../resources/LargeCanterburyCorpus/bible.txt"
    "../resources/LargeCanterburyCorpus/E.coli"
    "../resources/LargeCanterburyCorpus/world192.txt"
    "../resources/ProteinCorpus/hi"
    "../resources/ProteinCorpus/hs"
    "../resources/ProteinCorpus/mj"
    "../resources/ProteinCorpus/sc"))

(defun save-patterns (files output patterns-spec)
  (with-open-file (*standard-output*
                   output
                   :direction :output
                   :if-does-not-exist :create
                   :if-exists :overwrite)
    (iter (for file in files)
      (format t "~&~S ~{~S~^ ~}"
              file
              (gen-file-patterns file patterns-spec)))))

(defun try (patterns-file algorithm file-output patterns-spec)
  (with-output-to-file (*trace-output*
                        file-output
                        :if-does-not-exist :create
                        :if-exists :overwrite)
    (with-open-file (stream patterns-file
                            :direction :input
                            :if-does-not-exist :error)
      (iter (for file = (read stream nil 'eof))
        (until (eq file 'eof))
        (format *trace-output* "~&*************************")
        (format *trace-output* "~&~S" file)
        (format *trace-output* "~&##########")
        (format t "~&Start file: ~S" file)
        (iter
          (with text = (read-file-into-string file))
          (repeat (* (first patterns-spec)
                     (length (second patterns-spec))))
          (for pattern = (read stream))
          (format *trace-output* "~&~S:" pattern)
          (format t "~&Start with pattern: ~S" pattern)
          (time (funcall algorithm text pattern))
          (format *trace-output* "~&##########"))))))
