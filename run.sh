#!/bin/bash

echo "Run options:"
echo "1) Server"
echo "2) Client"
read -p "Select (1 or 2): " choice

if [ "$choice" == "1" ]; then
  java server.Server
elif [ "$choice" == "2" ]; then
  java client.GameUI
else
  echo "Invalid choice"
fi
