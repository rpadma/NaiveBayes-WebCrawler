import scrapy


class ImageSpider(scrapy.Spider):
    name = "getimage"

    def start_requests(self):
        url ='https://saimg-a.akamaihd.net/saatchi/426838/art/2435741/1505780-7.jpg'


        #for x in range(2, 10000):
        yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        page = response.url.split("/")[-2]
        filename = 'quotes-%s.txt' % page
        with open(filename, 'ab') as f:
            links=response.selector.xpath("//div[@class='list-art-image-wrapper']//a").xpath('@href').extract()
            for link in links:
             f.write("https://www.saatchiart.com"+link+"\n")

        self.log('Saved file %s' % filename)
