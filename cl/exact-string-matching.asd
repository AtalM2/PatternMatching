; Copyright (C) 2012 by Grégoire Jadi
; See the file LICENSE for copying permission.

(asdf:defsystem #:exact-string-matching
  :serial t
  :author "Grégoire Jadi"
  :license "MIT"
  :depends-on (#:alexandria
               #:iterate)
  :components ((:file "package")
               (:file "ebom")))

