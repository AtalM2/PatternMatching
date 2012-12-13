; Copyright (C) 2012 by Grégoire Jadi
; See the file LICENSE for copying permission.

(asdf:defsystem #:exact-string-matching
  :serial t
  :author "Grégoire Jadi"
  :license "MIT"
  :depends-on (#:alexandria
               #:iterate
               #:anaphora)
  :components ((:file "package")
               (:file "automata")
               (:file "bdm")
               (:file "ebom")

               (:file "sa.lisp")))

