#!/bin/bash

echo "Compiling project..."
javac common/*.java server/*.java client/*.java utils/*.java

if [ $? -eq 0 ]; then
  echo "Build successful."
else
  echo "Build failed."
fi
