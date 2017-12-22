Notes

All the code that I wrote is in Kotlin, which is an official language for Android development. (Just thought I should drop a note on this since the challenge spec mentions 'Java' - the main requirement is Android Studio, which I fulfilled.)

I expect that in 2 or so years, most new development will be in Kotlin -- much like the ObjC => Swift transformation that has taken place on iOS.

Other notes about differences to the spec / shortcomings. These are also mentioned in the code in comments in various places:

- 5a and 5c/d are disjoint (can't be done at same time)
- Profile images should be scaled to some product-defined size (creating a profile with large image can hang)
- 3a was dropped as being not needed (there is no explanation of its raison d'être)