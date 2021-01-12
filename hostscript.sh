#!/bin/bash


inotifywait -mrq --format '%e'  --event create /tmp/hls/
  while read file
  do
    sed -i '#EXTINF:5,' ${file}
  done
