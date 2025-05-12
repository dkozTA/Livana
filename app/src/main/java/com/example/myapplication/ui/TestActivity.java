package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Model.Booking.Booking;
import com.example.myapplication.data.Model.Review.Review;
import com.example.myapplication.data.Repository.Booking.BookingRepository;
import com.example.myapplication.data.Repository.Review.ReviewRepository;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "SearchTestActivity";

    private BookingRepository bookingRepository;
    private ReviewRepository reviewRepository;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bookingRepository = new BookingRepository(this);
        this.reviewRepository = new ReviewRepository(this);
        this.random = new Random();

        AtomicInteger index = new AtomicInteger();

        // Mảng 12 giá trị rating từ 1-5
        int[] ratings = {
                5, 4, 5, 3, 4, 5, 2, 1, 3, 4, 5, 2
        };

        // Mảng 12 nội dung đánh giá tương ứng
        String[] reviewContents = {
                "Tuyệt vời! Phòng đẹp, sạch sẽ và đầy đủ tiện nghi. Vị trí trung tâm, thuận tiện di chuyển.",             // 5 sao
                "Rất hài lòng với chỗ ở này. Chủ nhà thân thiện, nhiệt tình hỗ trợ mọi vấn đề.",                          // 4 sao
                "Xuất sắc! Căn hộ đúng như hình ảnh, view đẹp, nội thất mới. Sẽ quay lại lần sau.",                       // 5 sao
                "Ở mức trung bình, không có gì đặc biệt. Phòng sạch nhưng thiết bị hơi cũ.",                              // 3 sao
                "Khá tốt, giá cả hợp lý cho chất lượng nhận được. Vị trí thuận tiện.",                                    // 4 sao
                "Trải nghiệm tuyệt vời! Căn hộ rộng rãi, sạch sẽ và đầy đủ tiện nghi. Chủ nhà cực kỳ chu đáo.",          // 5 sao
                "Chưa đáp ứng kỳ vọng. Phòng nhỏ hơn trong hình và thiếu một số tiện nghi đã hứa.",                       // 2 sao
                "Thất vọng toàn tập. Phòng bẩn, ẩm mốc và không như mô tả. Chủ nhà không hỗ trợ khi yêu cầu.",           // 1 sao
                "Ổn, phòng đầy đủ tiện nghi cơ bản. Tuy nhiên hơi ồn vào buổi tối vì gần đường lớn.",                    // 3 sao
                "Rất tốt, chủ nhà thân thiện. Vị trí gần trung tâm, xung quanh nhiều tiện ích, dễ dàng di chuyển.",       // 4 sao
                "Hoàn hảo! Căn hộ sang trọng, sạch sẽ và đầy đủ tiện nghi. Chủ nhà rất thân thiện và chuyên nghiệp.",    // 5 sao
                "Nhiều vấn đề cần cải thiện. Vị trí xa trung tâm, thiếu tiện nghi cơ bản như đã quảng cáo."              // 2 sao
        };

        Review review = new Review("b42befb8-ee89-45a0-b79f-35777ee9d87f", "0d95b196-5340-41bd-9694-d6644736f8aa", ratings[5], reviewContents[5]);
        this.reviewRepository.guestReviewBooking(review, unused -> {
            Log.d(TAG, "Review created successfully");
        }, e -> {
            Log.e(TAG, "Error creating review: " + e.getMessage());
        });
    }
}