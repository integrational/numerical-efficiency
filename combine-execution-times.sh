#!/bin/bash

grep 'Time/s' */output_run | sed 's|/output_run||g' > combined_execution_times
