### 3.0.1 (2021-05-22)

* Fixed crash that happens when "The One Probe" is not installed ([7287114](https://github.com/nikita488/ZYCraft/commit/72871148f71bc8abea20173097a55d39180bdceb))
* Enabled blending for Fabricator GUI ([85ef65b](https://github.com/nikita488/ZYCraft/commit/85ef65b9ebc02716489200dc37df861a18d96cce))

### 3.0.0 (2021-05-22)

* Update to 1.16.5, ForgeGradle 4.1 ([e697e8f](https://github.com/nikita488/ZYCraft/commit/e697e8f545eb993fe203e5b05016d47e8ba0aeb7))
* Reworked code of Colorable blocks. Added IColorable ([74bc524](https://github.com/nikita488/ZYCraft/commit/74bc52401610e44bbab0d38a4a8772424bdc4bd3))
* Added tags for each color of Engineering Block, changed Basic Machines recipes to use this tags ([2b20414](https://github.com/nikita488/ZYCraft/commit/2b204149e0e6a3eb102402c6dcf08a7e6239bf89))
* Color Scanner now has a max stack size of 1 ([917a05f](https://github.com/nikita488/ZYCraft/commit/917a05f2582e8799a546336080e39acde48d8532))
* Added Fabricator, Valve (WIP), Item IO (WIP) ([e4b3049](https://github.com/nikita488/ZYCraft/commit/e4b3049f6bd6c7c0bc7d115de1ee70f5ed336cb8))
* Allow Zychorium Lamp connecting to on/off states ([76584b7](https://github.com/nikita488/ZYCraft/commit/76584b708c7dbecfb9032fd6c2d526d7382598c1))
* Changed recipe of Fire Basin ([f516f27](https://github.com/nikita488/ZYCraft/commit/f516f27b168269ed9c5551df8999f6d7813418ff))
* Reworked tooltips ([75ae6d5](https://github.com/nikita488/ZYCraft/commit/75ae6d5f8ad396712c3c071f390babff02ca7916))
* Moved Colorable blocks interaction to IColorable static helper method ([76f01ae](https://github.com/nikita488/ZYCraft/commit/76f01aecf0e80da807d2752a0410a6f05164cc2e))
* Rework of Aluminium Foil tooltip ([0f41f80](https://github.com/nikita488/ZYCraft/commit/0f41f80656357ca87eb5d34bf6302db79f81d236))
* Added isColorable static helper method to IColorable interface ([aa92f20](https://github.com/nikita488/ZYCraft/commit/aa92f20e03183847030a4b3e660ab228595b6fdd))

### 2.0.2 (2021-01-30)

* Fixed all bricks recipes had 1 output instead of 4 ([a281beb](https://github.com/nikita488/ZYCraft/commit/a281beb432c2504388c9191dcbeec094c178a26b))
* Added Zychorite to the forge:stone tag again ([6ac1dfc](https://github.com/nikita488/ZYCraft/commit/6ac1dfcf510a1c6b09598439caf4317816743093))
* Zychorium Shields now Wither and Dragon immune ([3fb91aa](https://github.com/nikita488/ZYCraft/commit/3fb91aa468bf6994ec954b68878ae7b24eced0fd))
* Added/changed/removed some recipes. Added missing item tags for storage blocks ([2ba9421](https://github.com/nikita488/ZYCraft/commit/2ba9421a21972acf545ca0090670281e221e3657))

### 2.0.1 (2020-12-01)

* Fixed trees not growing on Zychorium Soil. Closes #10 ([68da7aa](https://github.com/nikita488/ZYCraft/commit/68da7aa881123d6b77858acf11219edb9163081a))

### 2.0.0 (2020-11-30)

* Update to 1.16.4 ([1ba750a](https://github.com/nikita488/ZYCraft/commit/1ba750a9717ddfb607d25cc12253df3e7ee623d0))
* Fixed crash when crystal cluster placement finds 0 or negative surface height. Closes #9 ([b51630e](https://github.com/nikita488/ZYCraft/commit/b51630ee5d3a449f6763ef6e052fb4d75447c233))

### 1.1.1 (2020-11-02)

* Fixed not balanced stack sizes of fluid containers when they are filled with fluid ([add3d5c](https://github.com/nikita488/ZYCraft/commit/add3d5ca0153f1f2da13e85245d4959530980a08))

### 1.1.0 (2020-11-01)

* Added 3 fluid containers: Aluminium Foil, Quartz Bucket and Aluminium Can ([7dc9e87](https://github.com/nikita488/ZYCraft/commit/7dc9e87690a369e81c5bda7ff6345f0608af5191))
* Added Scythe ([270c004](https://github.com/nikita488/ZYCraft/commit/270c004d1c6c5ae5f8d4cdc0cbe7352eea64114a))
* Added secondary Cloud sprite for use in OBJ models (temporary) ([bd9677c](https://github.com/nikita488/ZYCraft/commit/bd9677c0c4120b6c3d39dd672f7d024353b581c5))
* Improved the logic of filling fluid containers when used on Zychorium Water ([62be522](https://github.com/nikita488/ZYCraft/commit/62be522caf770d640a693b98b9084e3c5df1a4df))
* Added BiomeDictionary support again ([6a023f9](https://github.com/nikita488/ZYCraft/commit/6a023f9f3124623433e5c5a1e2659dcae10ecea3))
* Reworked Basic Machines code a little bit, now it's much better. Added FluidAttributes support for placing fluid with fluid containers ([fef8a7e](https://github.com/nikita488/ZYCraft/commit/fef8a7eab8f8d55eb872b1654e2f79d9ca6c17ed))
* Register ConfiguredFeatures properly. Closes #7 ([18b88f3](https://github.com/nikita488/ZYCraft/commit/18b88f33ca660a1e1fdd091fc3d1f47ec66d8352))
* Refactored some recipes. Added new recipes for Zychorium Lamp. Added missing item tags ([bdf77c3](https://github.com/nikita488/ZYCraft/commit/bdf77c39dd07d2de3bc7ba91a4c3f9f771c3a863))

### 1.0.2 (2020-10-06)

* Added ability to use Bucket on Zychorium Water to get Water Bucket ([29b46a2](https://github.com/nikita488/ZYCraft/commit/29b46a21657df8fa46cecdf21ca7e9d7526be6b1))

### 1.0.1 (2020-10-04)

* Fixed Zychorium Soil does not make crops grow faster when stacked up ([12d0b26](https://github.com/nikita488/ZYCraft/commit/12d0b2653dd11846f21959349083476586f59962))

### 1.0.0 (2020-10-03)

* Changed version scheme until it's not too late ([769ba4d](https://github.com/nikita488/ZYCraft/commit/769ba4dab8c7b9ffcdc4cbbb5c920e58486bde2b))

### 0.1.5 (2020-10-03)

* Removed worldgen from biomes with NETHER and THEEND categories ([dc9f759](https://github.com/nikita488/ZYCraft/commit/dc9f759c439bdfd6ca329e1df5fff45e83d3e557))
* Allow Zychorite to be used in Stone Tools crafting recipes and in some others (Furnace, etc). Fixed non-opaque blocks had water drop particles at bottom face when water is placed on top of it ([115d941](https://github.com/nikita488/ZYCraft/commit/115d941404e24865f25891c7090e3f4e4b50c7b8))

### 0.1.4 (2020-10-03)

* Fixed Color Scanner tooltip ([f129522](https://github.com/nikita488/ZYCraft/commit/f12952205296a4529c1d4308d6ddd0aabb46f1cb))
* Removed debug worldgen json files which were accidentally added to the release version. Sorry! ([b257965](https://github.com/nikita488/ZYCraft/commit/b25796588cd5380625ac8e0ab0287fb0c7541910))

### 0.1.3 (2020-10-02)

* Initial release

