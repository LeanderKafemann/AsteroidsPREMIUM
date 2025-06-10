import sys

path = "/home/AsteroidsPREMIUM/serverScript"
if path not in sys.path:
    sys.path.append(path)

from serverScript import application