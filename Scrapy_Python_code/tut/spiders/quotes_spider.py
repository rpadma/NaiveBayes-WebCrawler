import scrapy


class QuotesSpider(scrapy.Spider):
    name = "quotes"

    def start_requests(self):
        url ='https://www.saatchiart.com/paintings/landscape?page='


        for x in range(2, 2128):
            yield scrapy.Request(url=url+str(x), callback=self.parse)

    def parse(self, response):
        page = response.url.split("/")[-2]
        filename = 'quotes-landscape1.txt'
        with open(filename, 'ab') as f:
            links=response.selector.xpath("//div[@class='list-art-image-wrapper']//a").xpath('@href').extract()
            for link in links:
             f.write("https://www.saatchiart.com"+link+"\n")

        self.log('Saved file %s' % filename)
