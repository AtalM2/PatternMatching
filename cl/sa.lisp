; Copyright (C) 2012 by Grégoire Jadi
; See the file LICENSE for copying permission.

(in-package #:exact-string-matching)

(defun sa (text pattern)
  (sa-find text pattern (remove-duplicates
                         (concatenate 'vector text pattern))))

(defun sa-find (text pattern alphabet)
  (let ((n (length text))
        (m (length pattern))
        (d (make-hash-table))
        (x (concatenate 'vector " " text))
        (p (concatenate 'vector " " pattern)))

    ;; Preprocess
    (iter (for letter in-vector alphabet)
      (setf (gethash letter d) m))

    (iter (for j from 1 below m)
      (setf (gethash (aref p j) d) (- m j)))

    ;; Search
    (iter (for i from m to n)
      (for k = i)
      (for j =
           (iter (for j from m above 0)
             (while (equalp (aref x k)
                            (aref p j)))
             (decf k)
             (finally (return j))))
      (when (zerop j)
        (collect (1+ k))))))
