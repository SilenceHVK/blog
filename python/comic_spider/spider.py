# -*- encoding:utf-8 -*-
import os
import random
import requests
import img2pdf
import shutil
from bs4 import BeautifulSoup
from concurrent.futures import ThreadPoolExecutor

index = '/chapter/576'
path = r'/Download/测试/'
base_url = 'http://www.92hm.top/'

user_agent = [
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6",
    "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6",
    "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1",
    "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
    "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
    "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
    "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
    "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3",
    "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24"
]


def getHeader(url):
    return {
        "User-Agent": random.choice(user_agent),
        "Referer": url
    }


def merge_img(chapter):
    file_name = chapter+".pdf"
    img_path = os.path.join(path, chapter)
    img_list = os.listdir(img_path)
    img_url_list = []
    for img in sorted(img_list, key=lambda x: int(x.split('.')[0])):
        img_url_list.append(os.path.join(img_path, img))

    with open(os.path.join(path, file_name), 'wb') as f:
        f.write(img2pdf.convert(img_url_list))
    shutil.rmtree(img_path)


def get_img(data):
    chapter = data[0]
    url = data[1]
    response = requests.get(url, headers=getHeader(url))
    response.encoding = 'UTF-8'
    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'lxml')

        # 如果文件夹不存在，则创建
        folderPath = f"{path}{chapter}"
        if not os.path.exists(folderPath):
            os.mkdir(folderPath)
        image_list = soup.find_all(attrs={"class": "lazy"})
        current_indx = 1
        for img in image_list:
            img_url = img.attrs["data-original"]
            img_name = os.path.basename(img_url)
            _, extension = os.path.splitext(img_url)
            img_content = requests.get(img_url, headers=getHeader(url)).content
            if len(img_content) != 0:
                with open(f"{folderPath}/{current_indx}{extension}", "wb") as f:
                    print(
                        f"{chapter} =>  正在下载图片 {img_name} 【{current_indx}/{len(image_list)}】")
                    f.write(img_content)
                    current_indx += 1
        print(f"==================章节下载完成 【{chapter}】 ====================")
    else:
        print(f"章节: {chapter} {url} 未找到")


if __name__ == '__main__':
    if not os.path.exists(path):
        os.mkdir(path)

    url = f"{base_url}{index}"
    response = requests.get(url, headers=getHeader(url))
    response.encoding = 'UTF-8'

    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'lxml')
        url_list = []
        for title in soup.find_all(attrs={"rel": "nofollow"}):
            url_list.append(
                (f"{title.string.strip()}", f"{base_url}{title.attrs['href']}"))
        with ThreadPoolExecutor(max_workers=8) as executor:
            executor.map(get_img, url_list)

        chapter_list = os.listdir(path)
        with ThreadPoolExecutor(max_workers=8) as executor:
            executor.map(merge_img, chapter_list)
        print(f"================== 漫画下载完成 ====================")

    else:
        print("未找到该地址")
