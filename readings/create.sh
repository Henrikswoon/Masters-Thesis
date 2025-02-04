#!/bin/bash
echo -n "Enter the name: "
read name
echo -n "Enter the link: "
read link

mkdir -p "$name"

echo "$link" > "$name/link"
cd "$name" || exit
nvim summary.md

