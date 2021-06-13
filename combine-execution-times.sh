#!/bin/bash

grep 'Time/s' */output_run | sed 's|/output_run||g' | sort -n -k2 > combined_execution_times
