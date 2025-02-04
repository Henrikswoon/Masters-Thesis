#!/bin/bash

get_previous_workday() {
  local day_of_week=$(date +%u)  
  local offset=1

  if [ "$day_of_week" -eq 1 ]; then
    offset=3      
  elif [ "$day_of_week" -eq 7 ]; then
    offset=2
  fi

  date -d "$offset day ago" +%d-%m-%y
}

# Default to today's date
DATE=$(date +%d-%m-%y)

# Check if --yesterday option is provided
if [ "$1" == "--yesterday" ]; then
  DATE=$(get_previous_workday)
fi

FILENAME="Entry_${DATE}.md"

if [ ! -f "$FILENAME" ]; then
  cat template > "$FILENAME"
fi

nvim "$FILENAME"
