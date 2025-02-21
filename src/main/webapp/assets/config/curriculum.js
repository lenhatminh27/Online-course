import { environment, STORAGE_KEY, avatarDefault } from '../../assets/config/env.js';
import { apiRequestWithToken } from '../../assets/config/service.js';

document.addEventListener('DOMContentLoaded', async function () {
    const userCurrent = localStorage.getItem(STORAGE_KEY.userCurrent);
    if (!userCurrent) {
        window.location.replace('/403');
        return;
    }
    const user = JSON.parse(userCurrent);
    if (!user?.roles?.includes('INSTRUCTOR')) {
        window.location.replace('/403');
        return;
    }
    const pathSegments = window.location.pathname.split('/');
    const curriculumIndex = pathSegments.indexOf("curriculum");
    let courseId = null;
    if (curriculumIndex !== -1 && curriculumIndex + 1 < pathSegments.length) {
        courseId = pathSegments[curriculumIndex + 1];
        console.log("Course ID:", courseId);
    } else {
        console.error("Không tìm thấy courseId trong URL");
        return;
    }
    // Khởi tạo Alpine.js
    document.querySelector('.content').setAttribute('x-data', `curriculumData`);
    window.curriculumData = {
        showFormLesson: false,
        showFormSection: false,
        sections: [],
        newSectionTitle: '',
        newSectionGoal: '',
        async loadLessons() {
            try {
                const response = await apiRequestWithToken(`/api/section/${courseId}`);
                this.sections = await response.json();
            } catch (error) {
                console.error('Lỗi tải dữ liệu:', error);
            }
        },
        async addSection() {
            if (!this.newSectionTitle.trim()) {
                alert('Vui lòng nhập tiêu đề');
                return;
            }
            try {
                const response = await apiRequestWithToken('/api/section', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        title: this.newSectionTitle,
                        goal: this.newSectionGoal
                    })
                });
                if (response.ok) {
                    this.newSectionTitle = '';
                    this.newSectionGoal = '';
                    this.showFormSection = false;
                    await this.loadLessons();
                } else {
                    alert('Thêm phần thất bại!');
                }
            } catch (error) {
                console.error('Lỗi khi thêm phần:', error);
            }
        }
    };

    // Gọi hàm loadLessons() khi trang load
    await curriculumData.loadLessons();
});
