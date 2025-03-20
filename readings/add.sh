#!/bin/bash
while true; do
	clear
	echo -n "Enter the name: "
	read name
	echo -n "Enter the link: "
	read link

	mkdir -p "$name"

	echo "$link" > "$name/link"
	
	echo -n "Continue adding? (*/n)"
	read answer

	if [[ "$answer" == "n" ]]; then
		break
	fi
done
