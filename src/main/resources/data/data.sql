-- ==========================================================
-- STEP 1: Insert 5 Users (Authors & Responders)
-- ==========================================================
INSERT INTO users (id, username) VALUES
                                     (1, 'tech_guru'),
                                     (2, 'travel_bug'),
                                     (3, 'pixel_art'),
                                     (4, 'chef_brian'),
                                     (5, 'fit_life');

-- ==========================================================
-- STEP 2: Insert 10 Posts
-- ==========================================================
INSERT INTO post (id, title, content, user_id, created_at) VALUES
                                                               (101, 'Unpopular Opinion: Microservices are overused', 'Starting a new project with microservices from day one is usually a trap. Build a clean monolith first.', 1, NOW()),
                                                               (102, 'My 3-week digital nomad experiment in Bali', 'Working remotely sounds like a dream, but dealing with power drops and spotty Wi-Fi is exhausting.', 2, NOW()),
                                                               (103, 'Stop using raw CSS hex codes in 2026', 'Switch over to CSS variables or HSL values. It makes managing dark/light themes infinitely easier.', 3, NOW()),
                                                               (104, 'The secret to restaurant-quality sourdough', 'It all comes down to ambient moisture. Bake your loaf inside a pre-heated Dutch oven to trap steam.', 4, NOW()),
                                                               (105, 'How to fix your posture at a desk', 'Forget ergonomic chairs. The single best fix is standing up to stretch for 30 seconds every hour.', 5, NOW()),
                                                               (106, 'Why I quit my consulting job with no backup', 'The 70-hour work weeks finally caught up to me. Mental health is a non-renewable asset.', 1, NOW()),
                                                               (107, 'Books that rewired my productivity logic', 'Atomic Habits is great, but Oliver Burkemans "Four Thousand Weeks" changes the entire paradigm.', 2, NOW()),
                                                               (108, 'Spring Boot performance optimizations', 'If you havent looked into Virtual Threads (Project Loom) yet, you are leaving massive wins behind.', 3, NOW()),
                                                               (109, 'Minimalist UI design that actually works', 'Minimalism doesnt mean hiding vital navigation. True minimalism means using whitespace as structure.', 4, NOW()),
                                                               (110, 'What nobody tells you about moving abroad', 'The efficiency is unmatched and safety is unbelievable, but the hidden administrative costs will shock you.', 5, NOW());

-- ==========================================================
-- STEP 3: Insert 5 Comments for EACH of the 10 Posts (50 Total)
-- ==========================================================
INSERT INTO comment (id, text, post_id, user_id) VALUES
-- Comments for Post 101
(1, 'Total agreement here. Kept it simple for my current launch.', 101, 2),
(2, 'But what about scaling data across teams later?', 101, 3),
(3, 'Monolith first is the pragmatic way to build.', 101, 4),
(4, 'Spent 6 months debugging networking tools instead of shipping features.', 101, 5),
(5, 'Underrated advice, saved to share with the team.', 101, 1),

(6, 'Bali is incredible, but the cafes get crowded fast.', 102, 1),
(7, 'Time zone differences are the silent productivity killer.', 102, 3),
(8, 'Did you check out the coworking spaces in Ubud?', 102, 4),
(9, 'Looks dreamy on Instagram, thanks for the reality check!', 102, 5),
(10, 'Im planning the exact same trip next month!', 102, 2),

-- Comments for Post 103
(11, 'HSL values changed how I code themes completely.', 103, 1),
(12, 'Can you share a quick tailwind config snippet for this?', 103, 2),
(13, 'Hex codes are just legacy habits at this point.', 103, 4),
(14, 'Makes auditing colors for accessibility much simpler too.', 103, 5),
(15, 'Exactly! Clean variable systems win every time.', 103, 3),

-- Comments for Post 104
(16, 'Dutch ovens are a total game changer for the home baker.', 104, 1),
(17, 'How long do you leave the lid on for steam generation?', 104, 2),
(18, 'Looks delicious! Sending this to my partner.', 104, 3),
(19, 'Tried it today and finally got that blistering open crumb!', 104, 5),
(20, 'Sourdough requires so much patience but it is worth it.', 104, 4),

-- Comments for Post 105
(21, 'Set a recurring timer on my laptop immediately.', 105, 1),
(22, 'My lower back thanks you for this simple reminder.', 105, 2),
(23, 'Movement over expensive gear, absolutely true.', 105, 3),
(24, 'Yoga blocks help tremendously during those hourly breaks.', 105, 4),
(25, 'Consistency beats ergonomic gear every single time.', 105, 5),

-- Comments for Post 106
(26, 'Brave move, sometimes you just have to jump.', 106, 2),
(27, 'Health is wealth. The rest can be rebuilt.', 106, 3),
(28, 'Wishing you the absolute best on your recovery break.', 106, 4),
(29, 'Did the exact same thing last year. Best choice I ever made.', 106, 5),
(30, 'Looking forward to seeing what you build next.', 106, 1),

-- Comments for Post 107
(31, 'Four Thousand Weeks completely destroyed my anxiety.', 107, 1),
(32, 'Adding both of these to my reading queue tonight.', 107, 3),
(33, 'Accepting limitation is the ultimate time management skill.', 107, 4),
(34, 'Atomic habits works well if paired with clear focus.', 107, 5),
(35, 'Phenomenal book recommendations right here.', 107, 2),

-- Comments for Post 108
(36, 'Loom makes highly concurrent apps incredibly lightweight.', 108, 1),
(37, 'Are you deploying native images on Docker containers?', 108, 2),
(38, 'Boot times are almost instantaneous now.', 108, 4),
(39, 'JPA connection pooling requires careful tuning with virtual threads.', 108, 5),
(40, 'Massive milestone for the Java ecosystem.', 108, 3),

-- Comments for Post 109
(41, 'Whitespace is literally active design material.', 109, 1),
(42, 'Hiding core menus under hamburger icons kills metrics.', 109, 2),
(43, 'If everything highlights, nothing stands out.', 109, 3),
(44, 'Clean, intentional typography does the heavy lifting.', 109, 5),
(45, 'Great perspective, clear UI beats fancy decorative fluff.', 109, 4),

-- Comments for Post 110
(46, 'Visa regulations alone require a dedicated spreadsheet.', 110, 1),
(47, 'The growth experience easily outweighs the paperwork overhead.', 110, 2),
(48, 'Finding a local community makes handling logistics much easier.', 110, 3),
(49, 'Highly accurate insight, relocation math gets complex fast.', 110, 4),
(50, 'Which country did you end up moving to?', 110, 5);

-- ==========================================================
-- STEP 4: Insert Random Likes across the 10 Posts
-- ==========================================================
INSERT INTO post_likes (id, post_id, user_id) VALUES
                                                  (1, 101, 2), (2, 101, 3), (3, 101, 5), -- Post 101 Likes
                                                  (4, 102, 1), (5, 102, 4),              -- Post 102 Likes
                                                  (6, 103, 1), (7, 103, 2), (8, 103, 5), -- Post 103 Likes
                                                  (9, 104, 3), (10, 104, 5),             -- Post 104 Likes
                                                  (11, 105, 1), (12, 105, 2), (13, 105, 3), (14, 105, 4), -- Post 105 Likes
                                                  (15, 106, 2), (16, 106, 5),            -- Post 106 Likes
                                                  (17, 107, 1), (18, 107, 4),            -- Post 107 Likes
                                                  (19, 108, 2), (20, 108, 5), (21, 108, 1), -- Post 108 Likes
                                                  (22, 109, 3),                          -- Post 109 Likes
                                                  (23, 110, 1), (24, 110, 2), (25, 110, 3), (26, 110, 4); -- Post 110 Likes


CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(50) NOT NULL UNIQUE
);

ALTER TABLE post ADD COLUMN category_id BIGINT;
ALTER TABLE post ADD CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES categories(id);

-- 1. Create the Categories
INSERT INTO categories (id, name) VALUES
                                      (1, 'Technology'),
                                      (2, 'Travel'),
                                      (3, 'Food & Lifestyle');

-- 2. Link your posts to Category IDs
UPDATE post SET category_id = 1 WHERE id IN (101, 103, 106, 108);
UPDATE post SET category_id = 2 WHERE id IN (102, 107, 110);
UPDATE post SET category_id = 3 WHERE id IN (104, 105, 109);


ALTER TABLE categories
    DROP COLUMN name;