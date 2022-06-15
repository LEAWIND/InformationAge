import numpy as np
import matplotlib.pyplot as plt
import os, random
from PIL import Image

FNAME = "assets/infage/textures/gui/codefield_background.png"
TW = int(1024 / 4)
TH = int(1024 / 4)
NNAME = "assets/infage/textures/gui/huge_codefield_background_%dx%d.png" % (TW,
                                                                            TH)


def main():
    img = Image.open(FNAME)
    im = np.array(img)
    cMap = {}
    for li in im:
        for c in li:
            tc = tuple(c)
            if tc in cMap:
                cMap[tc] += 1
            else:
                cMap[tc] = 1
    print(cMap)
    print(len(cMap))
    # nim = Image.new(mode=im.mode, size=(TW, TH))
    nim = np.zeros(shape=(TH, TW, 4), dtype=np.uint8)
    cArr = []
    for k in cMap:
        cArr += [np.array(k, dtype=np.uint8)] * cMap[k]

    for x in range(TW):
        for y in range(TH):
            nim[y][x] = random.choice(cArr)
    nimg = Image.fromarray(nim)
    nimg.save(NNAME, format="png")
    # plt.imshow(nim)
    # plt.show()


if __name__ == '__main__':
    main()
