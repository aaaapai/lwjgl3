/* SPDX-License-Identifier: MIT */
/* LWJGL: Manually created. This file is normally generated by configure. */
#ifndef LIBURING_COMPAT_H
#define LIBURING_COMPAT_H

#include <inttypes.h>
#include <stdint.h>
#include <sys/stat.h>

// LWJGL: skip <linux/time_types.h> include in io_uring.h
#define UAPI_LINUX_IO_URING_H_SKIP_LINUX_TIME_TYPES_H

typedef int __kernel_rwf_t;

#ifndef __ANDROID__
struct __kernel_timespec {
	int64_t		tv_sec;
	long long	tv_nsec;
};

struct open_how {
	uint64_t	flags;
	uint64_t	mode;
	uint64_t	resolve;
};
#endif

#endif
