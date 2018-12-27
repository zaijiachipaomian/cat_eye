$(document).ready(function () {
    'use strict';

    //
    // 获取主页消息
    function fetch_index() {
        $.get("/index_data/todayBoxOffice", {}, function (data, status) {

            data = JSON.parse(data);
            var list = data.data.list;
            //console.log(list);
            for (let i = 0; i < list.length; i++) {
                if (i == 10) {
                    break;
                }
                let obj = list[i];
                unpack_today_box_office(obj, i + 1);
                console.log(obj.movieId + "  " + obj.movieName + " rank " + i + 1);
            }
        });
    }


    // 将今日票房的信息写入dom文档
    function unpack_today_box_office(movie, rank) {
        // todo
        console.log("rank = ", rank);

        $(".right-pannel").append(` <li class="ranking-item ranking-index-${rank}">
                        <a href="/films/${movie.movieId}"  data-act="ticketList-movie-click" >
                            <span class="normal-link-${rank}" >
                                <i class="ranking-index">${rank}</i>
                                <span class="ranking-movie-name">${movie.movieName}</span>
                                <span class="ranking-num-info">
                                    <span class="stonefont">${movie.boxInfo}</span>万
                                </span>
                            </span>
                        </a>
                    </li>`);


    }

    fetch_index();
});