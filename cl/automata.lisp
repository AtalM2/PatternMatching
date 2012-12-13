; Copyright (C) 2012 by Grégoire Jadi
; See the file LICENSE for copying permission.

(in-package #:exact-string-matching)

(defclass fsa ()
  ((states                 :accessor get-states                 :initarg :states)
   (initial-state          :accessor get-initial-state          :initarg :initial-state)
   (accepting-states       :accessor get-accepting-states       :initarg :accepting-states)
   (alphabet               :accessor get-alphabet               :initarg :alphabet)
   (state-transition-table :accessor get-state-transition-table :initarg :state-transition-table)))


(defmethod delta (automata state letter)
  "The Delta function: δ : Q x Σ → Q"
  (aref (get-state-transition-table automata)
        (gethash state
                 (get-states automata))
        (gethash letter
                 (get-alphabet automata))))

(defun make-index (list)
  "Makes a reverse index of the given list."
  (iter (with ht = (make-hash-table))
    (for el in list)
    (for i upfrom 0)
    (setf (gethash el ht) i)
    (finally (return ht))))

(defun make-fsa (&key states initial-state accepting-states alphabet state-transition-table)
  (make-instance
   'fsa
   :states (make-index states)
   :initial-state initial-state
   :accepting-states (iter (with ht = (make-hash-table))
                       (for state in states)
                       (setf (gethash state ht) (member state accepting-states))
                       (finally (return ht)))
   :alphabet (make-index alphabet)
   :state-transition-table
   (make-array (list (length states)
                     (length alphabet))
               :initial-contents
               state-transition-table)))

(defparameter automata
  (make-fsa :states (iota 10)
            :initial-state 7
            :accepting-states (iota 10)
            :alphabet '(a b)
            :state-transition-table
            '((nil nil)
              (nil 0)
              (1 nil)
              (2 nil)
              (nil 3)
              (nil 4)
              (1 5)
              (6 8)
              (2 9)
              (2 3))))


