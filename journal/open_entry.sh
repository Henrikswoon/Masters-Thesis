#!/bin/bash
DATE=$(date +%d-%m-%y)
FILENAME="Entry_${DATE}.md"
if [ ! -f "$FILENAME" ]; then
	cat template > "$FILENAME"
fi
nvim "$FILENAME"

